"""
JIDP taobao view.
URLs include:
/taobao.html
"""
import flask
from flask import request
from flask import session
from flask import redirect
from flask import url_for
import numpy as np
import os
import matplotlib.pyplot as plt
import jidp


@jidp.app.route('/taobao', methods=['GET'])
def show_taobao():
    """Display / route."""
    return flask.render_template("taobao.html")
