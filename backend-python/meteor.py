from nltk.translate.meteor_score import meteor_score, single_meteor_score


def calculate_meteor(reference, candidate):
    score = round(single_meteor_score(reference, candidate), 4)
    return score
