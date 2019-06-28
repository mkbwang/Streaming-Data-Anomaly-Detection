"""REST API for JIDP"""
import flask
import os
from flask import Flask, send_file
import numpy as np
import io
import jidp
import base64
import matplotlib.pyplot as plt

@jidp.app.route('/api/heatmap/',methods=["GET"])
def get_heatmap():
    directory = jidp.model.get_rainfall() # get the directory
    # feedback = flask.request.get_json() # get the json file
    filename = "currentrain.npy"
    total_directory = os.path.join(directory, filename)
    data = np.load(total_directory)# load the matrix
    # plot the heatmap
    fig, ax = plt.subplots()
    im = ax.imshow(data, cmap=plt.get_cmap('Reds'), interpolation='nearest',
                   vmin=0, vmax=80)
    fig.colorbar(im)
    # store it as a base64 stream and send it
    strIO = io.BytesIO()
    plt.savefig(strIO, format='png')
    plt.close()
    strIO.seek(0)
    plotcode = base64.encodestring(strIO.read())
    return plotcode.decode('utf-8'), 200, {'Content-Type': 'text/plain'}
