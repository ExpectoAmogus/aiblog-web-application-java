from flask import Flask, request, jsonify

from bleu import calculate_bleu
from rouge import calculate_rouge

app = Flask(__name__)


@app.route('/bleu', methods=["POST"])
def bleu():
    data = request.get_json()

    if 'reference' in data and 'candidate' in data:
        reference_sentence = data['reference']
        candidate_sentence = data['candidate']

        bleu_score = calculate_bleu(reference_sentence, candidate_sentence)

        response = {
            'bleu_score': bleu_score
        }

        return jsonify(response), 201

    else:
        return jsonify(
            {'error': 'Invalid request. Make sure to provide both "reference" and "candidate" sentences.'}), 400


@app.route('/rouge', methods=["POST"])
def rouge():
    data = request.get_json()

    if 'reference' in data and 'candidate' in data:
        reference_sentence = data['reference']
        candidate_sentence = data['candidate']

        rouge_scores = calculate_rouge(reference_sentence, candidate_sentence)

        response = {}
        for key, value in rouge_scores.items():
            response[key] = {
                'precision': value.precision,
                'recall': value.recall,
                'fmeasure': value.fmeasure
            }

        return jsonify(response), 201

    else:
        return jsonify(
            {'error': 'Invalid request. Make sure to provide both "reference" and "candidate" sentences.'}), 400


if __name__ == '__main__':
    app.run()
