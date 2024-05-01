import os
import pandas as pd

from bleu import calculate_bleu
from meteor import calculate_meteor
from rouge import calculate_rouge


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


# Function for processing prompts and writing results to CSV files
def process_prompts(prompt_data, model_name, request_func, reference_data):
    results = []

    for prompt in prompt_data['prompts']:
        reference = reference_data.loc[prompt_data['prompts'].index(prompt), 'article_text']
        candidate = request_func(prompt)  # Getting candidate from the model

        evaluation_results = evaluate_text_quality(reference, candidate)
        results.append({'prompt': prompt.strip(), 'reference': reference, 'candidate': candidate,
                        'evaluation_results': evaluation_results})

    # Writing results to CSV file
    df = pd.DataFrame(results)
    os.makedirs(f'{model_name}', exist_ok=True)
    df.to_csv(f'{model_name}/results_{prompt_data["file"]}.csv', index=False)


# Function for requesting GPT-3.5 model API
def request_gpt_3_5(prompt):
    # Implementation to request GPT-3.5 model API
    pass


def request_claude(prompt):
    # Implementation to request Claude model API
    pass


def request_cohere(prompt):
    # Implementation to request Cohere model API
    pass


# Loading prompts from each file
prompt_files = ['prompts_1.txt', 'prompts_2.txt', 'prompts_3.txt']
prompts_data = []

for prompt_file in prompt_files:
    with open(prompt_file, 'r', encoding='utf-8') as file:
        prompts_data.append({'file': prompt_file, 'prompts': file.readlines()})

# Loading data from file with article_text column
reference_data = pd.read_csv('../parser_result.csv')

# Processing prompts and writing results for each model
for prompt_data in prompts_data:
    process_prompts(prompt_data, 'gpt_3_5', request_gpt_3_5, reference_data)
    process_prompts(prompt_data, 'claude', request_claude, reference_data)
    process_prompts(prompt_data, 'cohere', request_cohere, reference_data)
