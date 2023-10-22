from nltk.translate.bleu_score import sentence_bleu, SmoothingFunction


def calculate_bleu(reference_sentence, candidate_sentence):
    reference = reference_sentence.split()
    candidate = candidate_sentence.split()

    smooth = SmoothingFunction()

    bleu_score = sentence_bleu([reference], candidate, smoothing_function=smooth.method1)

    return bleu_score
