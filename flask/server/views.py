from flask import Blueprints

wandersafe = Blueprint('wandersafe', __name__)

@wandersafe.route('/tell_me_if_im_going_to_die')
def tell_me_if_im_going_to_die():
    return 'Probably'