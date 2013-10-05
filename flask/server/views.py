from flask import request
from .stackmob import get_crime_near

import logging
logging.basicConfig(level=logging.DEBUG)

def tell_me_if_im_going_to_die():
    status= get_crime_near(float(request.args['longitude']),
                           float(request.args['latitude']),
                           float(request.args['meters']))
    logging.debug(status)
    return '{}'.format(status)