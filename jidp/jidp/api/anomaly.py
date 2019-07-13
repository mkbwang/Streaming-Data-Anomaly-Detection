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

@jidp.app.route('/api/anomaly/',methods=["GET"])
def get_anomaly():
    directory = jidp.model.get_rainfall() # get the directory
    a1 = "a1.json"
    a1_directory = os.path.join(directory, a1)
    with open(a1_directory, 'r') as f:
        anomaly1 = json.load(f)
    a2 = "a2.json"
    a2_directory = os.path.join(directory, a2)
    with open(a2_directory, 'r') as f:
        anomaly2 = json.load(f)
    output = {"a1": anomaly1["anomaly"], "a2": anomaly2["anomaly"]}
    return jsonify(output), 200
