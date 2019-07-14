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
import psycopg2 as pg
from flask import jsonify

@jidp.app.route('/api/customer/',methods=["GET"])
def get_customer():
    directory = jidp.model.get_rainfall() # get the directory
    standardfile = os.path.join(directory, "customerstandard.json")
    with open(standardfile, 'r') as f:
        standard = json.load(f)
    totalstandards = []
    if standard["userbrand"]:
        totalstandards.append("anomalytype='brand'")
    if standard["userproduct"]:
        totalstandards.append("anomalytype='product'")
    if standard["usercategory"]:
        totalstandards.append("anomalytype='category'")
    statement = " OR ".join(totalstandards)
    sql = "SELECT * FROM customer WHERE "+statement +"ORDER BY inserttime DESC LIMIT 10"
    completename = os.path.join(directory, filename)
    with open(completename, 'r') as f:
        output = json.load(f)
    return jsonify(output), 200
