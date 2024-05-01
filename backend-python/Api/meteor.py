from nltk import word_tokenize
from nltk.translate.meteor_score import meteor_score, single_meteor_score
import nltk

nltk.download('punkt')
nltk.download('wordnet')


def calculate_meteor(reference, candidate):
    reference_tokens = word_tokenize(reference.lower())
    candidate_tokens = word_tokenize(candidate.lower())

    score = round(single_meteor_score(reference_tokens, candidate_tokens), 4)
    return score
