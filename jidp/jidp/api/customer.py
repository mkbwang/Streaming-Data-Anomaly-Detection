"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import json
import pickle
from flask import jsonify

@jidp.app.route('/api/customer/',methods=["GET"])
def get_customer():
    directory = jidp.model.get_rainfall() # get the directory
    thresholdfile = os.path.join(directory, "customerthreshold.pkl")
    with open(thresholdfile, 'rb') as f:
        threshold = pickle.load(thresholdfile)
    filename = "customer.json"
    completename = os.path.join(directory, filename)
    with open(completename, 'r') as f:
        output = json.load(f)
    return jsonify(output), 200
