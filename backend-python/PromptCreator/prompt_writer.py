import pandas as pd
import os

# Load headlines from the file
headlines_df = pd.read_csv('../parser_result.csv')
headlines = headlines_df['headline'].tolist()

# Define the prompt files
prompt_files = ['prompts_1.txt', 'prompts_2.txt', 'prompts_3.txt']

# Process each prompt file
for prompt_file in prompt_files:
    # Read the original prompt text
    with open(prompt_file, 'r') as file:
        original_prompt = file.read()

    # Write modified prompts to a separate text file
    output_file = f'modified_prompts_{os.path.splitext(prompt_file)[0]}.txt'
    with open(output_file, 'w') as file:
        for i, headline in enumerate(headlines, start=1):
            modified_prompt = original_prompt.replace('"This Ain\'t It, Chief" Updated To "This Ain\'t It, Noble '
                                                      'Indigenous Tribal Leader."', f'"{headline}"')
            file.write(modified_prompt + '\n')
