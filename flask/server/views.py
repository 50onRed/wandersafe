from flask import request, render_template
from .stackmob import get_crime_near


def tell_me_if_im_going_to_die():
    response = get_crime_near(float(request.args['latitude']),
                              float(request.args['longitude']),
                              float(request.args['meters']))
    level = process_crime_level(response)
    return str(level)

def show_map():
    lat = float(request.args['latitude'])
    long = float(request.args['longitude'])
    response = get_crime_near(lat, long, float(request.args['meters']))
    return render_template("map.html", lat=lat, long=long, crimes=response)

def process_crime_level(crimes):
    # This level is not correct
    # ucr_general is the crime level code, not the num of occurences
    # TODO: Fix the method of calculating a level
    if len(crimes) == 0:
        return 0 # avoid division by zero
    total = sum([int(crime['ucr_general']) / 100 for crime in crimes])
    return total / len(crimes)
        
