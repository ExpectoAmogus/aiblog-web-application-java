from flask import Flask, request, jsonify
import matplotlib.pyplot as plt
import io
import base64

from bleu import calculate_bleu
from meteor import calculate_meteor
from rouge import calculate_rouge

app = Flask(__name__)
root = '/api/v1/python'

bleu_scores = []
rouge_f1_scores = []
meteor_scores = []
iteration_numbers_bleu = []
iteration_numbers_rouge = []
iteration_numbers_meteor = []


@app.route(root + '/bleu', methods=["POST"])
def bleu():
    data = request.get_json()

    if 'reference' in data and 'candidate' in data and 'iterations' in data:
        reference_sentence = data['reference']
        candidate_sentence = data['candidate']
        iterations = data.get('iterations', 10)

        bleu_scores.clear()
        iteration_numbers_bleu.clear()

        for _ in range(iterations):
            bleu_score = calculate_bleu(reference_sentence, candidate_sentence)

            bleu_scores.append(bleu_score)
            iteration_numbers_bleu.append(len(bleu_scores))

        response = {
            'bleu_scores': bleu_scores
        }

        return jsonify(response), 201

    else:
        return jsonify(
            {'error': 'Invalid request. Make sure to provide both "reference" and "candidate" sentences.'}), 400


@app.route(root + '/rouge', methods=["POST"])
def rouge():
    data = request.get_json()

    if 'reference' in data and 'candidate' in data and 'iterations' in data:
        reference_sentence = data['reference']
        candidate_sentence = data['candidate']
        iterations = data.get('iterations', 10)

        rouge_f1_scores.clear()
        iteration_numbers_rouge.clear()

        for _ in range(iterations):
            rouge_scores = calculate_rouge(reference_sentence, candidate_sentence)

            rouge_f1_scores.append(rouge_scores)
            iteration_numbers_rouge.append(len(rouge_f1_scores))

        response = {
            'rouge_scores': rouge_f1_scores
        }

        return jsonify(response), 201

    else:
        return jsonify(
            {'error': 'Invalid request. Make sure to provide both "reference" and "candidate" sentences.'}), 400


@app.route(root + '/meteor', methods=["POST"])
def meteor():
    data = request.get_json()

    if 'reference' in data and 'candidate' in data and 'iterations' in data:
        reference_sentence = data['reference']
        candidate_sentence = data['candidate']
        iterations = data.get('iterations', 10)

        meteor_scores.clear()
        iteration_numbers_meteor.clear()

        for _ in range(iterations):
            meteor_score = calculate_meteor(reference_sentence, candidate_sentence)

            meteor_scores.append(meteor_score)
            iteration_numbers_meteor.append(len(meteor_scores))

        response = {
            'meteor_scores': meteor_scores
        }

        return jsonify(response), 201

    else:
        return jsonify(
            {'error': 'Invalid request. Make sure to provide both "reference" and "candidate" sentences.'}), 400


@app.route(root + '/plot-bleu', methods=["GET"])
def plot_bleu():
    plt.figure(figsize=(10, 6))
    plt.plot(iteration_numbers_bleu, bleu_scores, label='BLEU')
    plt.xlabel('Iteration')
    plt.ylabel('Score')
    plt.title('Quality Evaluation Over Iterations')
    plt.legend()
    plt.grid(True)

    img_buffer = io.BytesIO()
    plt.savefig(img_buffer, format='png')
    img_buffer.seek(0)

    img_base64 = base64.b64encode(img_buffer.read()).decode()

    response = {
        'img_base64': img_base64
    }

    return jsonify(response), 200


@app.route(root + '/plot-rouge', methods=["GET"])
def plot_rouge():
    plt.figure(figsize=(10, 6))
    plt.plot(iteration_numbers_rouge, rouge_f1_scores, label='ROUGE-L F1')
    plt.xlabel('Iteration')
    plt.ylabel('Score')
    plt.title('Quality Evaluation Over Iterations')
    plt.legend()
    plt.grid(True)

    img_buffer = io.BytesIO()
    plt.savefig(img_buffer, format='png')
    img_buffer.seek(0)

    img_base64 = base64.b64encode(img_buffer.read()).decode()

    response = {
        'img_base64': img_base64
    }

    return jsonify(response), 200


@app.route(root + '/plot-meteor', methods=["GET"])
def plot_meteor():
    plt.figure(figsize=(10, 6))
    plt.plot(iteration_numbers_meteor, meteor_scores, label='METEOR')
    plt.xlabel('Iteration')
    plt.ylabel('Score')
    plt.title('Quality Evaluation Over Iterations')
    plt.legend()
    plt.grid(True)

    img_buffer = io.BytesIO()
    plt.savefig(img_buffer, format='png')
    img_buffer.seek(0)

    img_base64 = base64.b64encode(img_buffer.read()).decode()

    response = {
        'img_base64': img_base64
    }

    return jsonify(response), 200


if __name__ == '__main__':
    app.run()
