import os
import pandas as pd
import anthropic
import cohere
import spacy
from openai import OpenAI
import json
import matplotlib.pyplot as plt
from scipy.stats import f_oneway

from bleu import calculate_bleu
from meteor import calculate_meteor
from rouge import calculate_rouge

nlp = spacy.load("en_core_web_sm")


# Function for evaluating text quality and writing results to a JSON file
def evaluate_text_quality(reference, candidate):
    bleu_score = calculate_bleu(reference, candidate)
    meteor_score = calculate_meteor(reference, candidate)
    rouge_scores = calculate_rouge(reference, candidate)

    results = {
        'BLEU': bleu_score,
        'METEOR': meteor_score,
        'ROUGE': rouge_scores
    }

    return results


# Function for analyzing prompt complexity
def analyze_prompt_complexity(prompts):
    prompt_complexities = []
    for prompt in prompts:
        doc = nlp(prompt)

        # Calculate various complexity factors, such as sentence length, presence of specialized terms, etc.
        complexity_score = 0

        # Example factors for assessing complexity:
        sentence_length = len(list(doc.sents))  # Sentence length
        num_entities = len(doc.ents)  # Number of named entities
        num_complex_words = sum(1 for token in doc if token.is_alpha and len(token.text) > 6)  # Number of complex words

        # Additional metrics can be added as needed

        # Simple formula for calculating complexity based on metrics
        complexity_score = sentence_length + num_entities + num_complex_words

        prompt_complexities.append(complexity_score)

    return prompt_complexities


# Function for processing prompts and writing results to CSV files
def process_prompts(prompt_data, model_name, request_func, reference_data, api_key):
    results = []
    prompt_complexities = analyze_prompt_complexity(prompt_data['prompts'])

    for prompt, complexity in zip(prompt_data['prompts'], prompt_complexities):
        reference = reference_data.loc[prompt_data['prompts'].index(prompt), 'article_text']
        candidate = request_func(prompt, api_key)  # Getting candidate from the model

        evaluation_results = evaluate_text_quality(reference, candidate)
        results.append({'prompt': prompt.strip(), 'prompt_complexity': complexity,
                        'reference': reference, 'candidate': candidate,
                        'BLEU': evaluation_results['BLEU'],
                        'METEOR': evaluation_results['METEOR'],
                        'ROUGE1': evaluation_results['ROUGE']['rouge1'].precision,
                        'ROUGE2': evaluation_results['ROUGE']['rouge2'].precision,
                        'ROUGEL': evaluation_results['ROUGE']['rougeL'].precision})

    # Writing results to CSV file
    df = pd.DataFrame(results)
    os.makedirs(f'{model_name}', exist_ok=True)
    df.to_csv(f'{model_name}/results_{prompt_data["file"]}.csv', index=False)




# Function for requesting GPT-3.5 model API
def request_gpt_3_5(prompt, api_key):
    client = OpenAI(
        api_key=api_key,
    )
    response = client.chat.completions.create(
        model="gpt-3.5-turbo-0125",
        messages=[
            {"role": "system", "content": "You are a satirical article writer."},
            {"role": "user", "content": prompt},
        ]
    )
    return response.choices[0].message.content.replace('\n', '').replace('\r', '').replace('\t', '')


# Function for requesting Claude model API
def request_claude(prompt, api_key):
    client = anthropic.Anthropic(
        # api_key=os.environ.get("ANTHROPIC_API_KEY"),
        api_key=api_key,
    )
    message = client.messages.create(
        model="claude-3-opus-20240229",
        max_tokens=1000,
        temperature=0,
        messages=[
            {
                "role": "user",
                "content": [
                    {
                        "type": "text",
                        "text": prompt
                    }
                ]
            }
        ]
    )
    return message.content[0].text.replace('\n', '').replace('\r', '').replace('\t', '')


# Function for requesting Cohere model API
def request_cohere(prompt, api_key):
    co = cohere.Client(
        # api_key=os.environ.get("COHERE_API_KEY"),
        api_key=api_key,
    )

    chat = co.chat(
        message=prompt,
        model="command"
    )

    return chat.text.replace('\n', '').replace('\r', '').replace('\t', '')


# Loading prompts from each file
# prompt_files = ['prompts_1.txt', 'prompts_2.txt', 'prompts_3.txt']
prompt_files = ['prompts_4.txt']
prompts_data = []

with open('api_keys.json', 'r') as file:
    api_keys = json.load(file)

for prompt_file in prompt_files:
    with open(prompt_file, 'r', encoding='utf-8') as file:
        prompts_data.append({'file': prompt_file, 'prompts': file.readlines()})

# Loading data from file with article_text column
reference_data = pd.read_csv('../parser_result.csv')

# Processing prompts and writing results for each model
for prompt_data in prompts_data:
    process_prompts(prompt_data, 'gpt_3_5', request_gpt_3_5, reference_data, api_keys['openai_api_key'])
    process_prompts(prompt_data, 'claude', request_claude, reference_data, api_keys['anthropic_api_key'])
    process_prompts(prompt_data, 'cohere', request_cohere, reference_data, api_keys['cohere_api_key'])
