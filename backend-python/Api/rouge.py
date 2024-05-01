from rouge_score import rouge_scorer


def calculate_rouge(reference_summary, candidate_summary):
    scorer = rouge_scorer.RougeScorer(['rouge1', 'rouge2', 'rougeL'], use_stemmer=True)

    scores = scorer.score(reference_summary, candidate_summary)

    return scores
