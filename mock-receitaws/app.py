#!/usr/bin/env python3
"""
Mock HTTP Server para ReceitaWS
Retorna sucesso para CPFs que terminam com dígito par, erro para ímpares
"""
from flask import Flask, jsonify, request
import sys

app = Flask(__name__)

@app.route('/v1/cpf/<cpf>', methods=['GET'])
def validar_cpf(cpf):
    """Valida CPF - retorna sucesso se terminar com dígito par"""
    # Remove formatação
    cpf_limpo = cpf.replace('.', '').replace('-', '')
    
    # CPFs que terminam com dígito par são válidos
    ultimo_digito = int(cpf_limpo[-1])
    valido = ultimo_digito % 2 == 0
    
    if valido:
        return jsonify({
            "status": "OK",
            "cpf": cpf_limpo,
            "nome": f"Pessoa Teste {cpf_limpo[:3]}",
            "nascimento": "01/01/1990",
            "situacao": "Regular",
            "erro": []
        }), 200
    else:
        return jsonify({
            "status": "ERROR",
            "erro": ["CPF não encontrado na base de dados"]
        }), 404

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "OK"}), 200

if __name__ == '__main__':
    port = int(sys.argv[1]) if len(sys.argv) > 1 else 8082
    app.run(host='0.0.0.0', port=port, debug=False)
