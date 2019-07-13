"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
from flask import jsonify

@jidp.app.route('/api/othersupdate/',methods=["POST"])
def get_othersupdate():
    feedback = flask.request.get_json()
    print(feedback)
    output = {"status":"good"}
    return jsonify(output), 201
