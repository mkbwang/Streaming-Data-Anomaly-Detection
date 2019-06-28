import flask
import jidp

def get_rainfall():
    return jidp.app.config['RAINFALL_FILENAME']