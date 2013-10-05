from flask import request
from .stackmob import get_crime_near


def tell_me_if_im_going_to_die():
    response = get_crime_near(float(request.args['latitude']),
                              float(request.args['longitude']),
                              float(request.args['meters']))
    level = process_crime_level(response)
    # This level is not correct
    # ucr_general is the crime level code, not the num of occurences
    # TODO: Fix the method of calculating a level
    return str(level)

def process_crime_level(crimes):
    if len(crimes) == 0:
        return 0 # avoid division by zero
    total = sum([int(crime['ucr_general']) / 100 for crime in crimes])
    return total / len(crimes)
        
