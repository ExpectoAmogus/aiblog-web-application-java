import os
import pandas as pd
import matplotlib.pyplot as plt
from scipy.stats import f_oneway


# Function to merge results from multiple files into one
def merge_result_files(model_name, file_list, output_file):
    dfs = []
    for file in file_list:
        df = pd.read_csv(f'{model_name}/{file}')
        dfs.append(df)
    merged_df = pd.concat(dfs, ignore_index=True)
    os.makedirs(f'{model_name}', exist_ok=True)
    merged_df.to_csv(f'{model_name}/{output_file}', index=False)
    return merged_df


# Function to perform ANOVA on the results
def perform_anova(data, column_name, complexity_column='prompt_complexity'):
    complexity_levels = data[complexity_column].unique()
    groups = [data[data[complexity_column] == level][column_name] for level in complexity_levels]

    f_statistic, p_value = f_oneway(*groups)
    return f_statistic, p_value


# File lists for each model
file_lists = {
    'gpt_3_5': ['results_prompts_1.txt.csv', 'results_prompts_2.txt.csv', 'results_prompts_3.txt.csv',
                'results_prompts_4.txt.csv'],
    'claude': ['results_prompts_1.txt.csv', 'results_prompts_2.txt.csv', 'results_prompts_3.txt.csv',
               'results_prompts_4.txt.csv'],
    'cohere': ['results_prompts_1.txt.csv', 'results_prompts_2.txt.csv', 'results_prompts_3.txt.csv',
               'results_prompts_4.txt.csv']
}

# Merging files and performing ANOVA for each model and each metric
for model_name, files in file_lists.items():
    merged_data = merge_result_files(model_name, files, 'merged_results.csv')

    for metric in ['BLEU', 'METEOR', 'ROUGE1', 'ROUGE2', 'ROUGEL']:
        f_stat, p_val = perform_anova(merged_data, metric)
        print(f'ANOVA results for {metric} in {model_name}:')
        print(f'F-statistic: {f_stat}')
        print(f'p-value: {p_val}')

        # Plotting the results
        plt.figure(figsize=(10, 5))
        plt.scatter(merged_data['prompt_complexity'], merged_data[metric], alpha=0.5)
        plt.title(f'{metric} vs Prompt Complexity for {model_name}')
        plt.xlabel('Prompt Complexity')
        plt.ylabel(metric)
        plt.grid(True)
        plt.savefig(f'{model_name}/{metric}_vs_prompt_complexity.png')
        plt.close()
