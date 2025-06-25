from flask import Flask, request, jsonify
from flask_cors import CORS
from deep_translator import GoogleTranslator

app = Flask(__name__)
CORS(app)

# Lista de idiomas soportados en tu menú
IDIOMAS_PERMITIDOS = [
    'es', 'en', 'de', 'pt', 'fr', 'zh-CN', 'ru', 'ar', 'ja', 'hi', 'it', 'ko', 'nl', 'pl', 'tr'
]

@app.route('/traducir', methods=['POST'])
def traducir():
    data = request.get_json()
    texto = data.get('texto')
    idioma = data.get('idioma', 'en')  # Por defecto, inglés

    if not texto:
        return jsonify({'error': 'Texto no proporcionado'}), 400

    if idioma not in IDIOMAS_PERMITIDOS:
        return jsonify({'error': f"Idioma '{idioma}' no está permitido"}), 400

    try:
        traduccion = GoogleTranslator(source='auto', target=idioma).translate(text=texto)
        return jsonify({'traduccion': traduccion})
    except Exception as e:
        return jsonify({'error': f'Error en la traducción: {str(e)}'}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

