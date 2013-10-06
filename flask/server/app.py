from flask import Flask
from .views import index, tell_me_if_im_going_to_die, get_crimes, show_map, reset_debug_level

def create_app():
    app = Flask(__name__)
    app.config.from_pyfile('config_default.py')
    app.add_url_rule('/', 'index', index)
    app.add_url_rule('/level/<lat>/<lon>/<meters>', 'tell_me_if_im_going_to_die', tell_me_if_im_going_to_die)
    app.add_url_rule('/map/<lat>/<lon>', 'show_map', show_map)
    app.add_url_rule('/crimes/<lat>/<lon>/<meters>', 'get_crimes', get_crimes)
    app.add_url_rule('/reset/<level>', 'reset_debug_level', reset_debug_level)
    return app
