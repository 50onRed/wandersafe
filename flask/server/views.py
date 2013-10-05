from flask import request, render_template
from .stackmob import get_crime_near

def tell_me_if_im_going_to_die(lat, lon, meters):
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    level = process_crime_level(response)
    return str(level)

def show_map(lat, lon, meters):
    lat, lon, meters = float(lat), float(lon), float(meters)
    response = get_crime_near(lat, lon, meters)
    return render_template("map.html", lat=lat, long=lon, crimes=response)

def process_crime_level(crimes):
    # This level is not correct
    # ucr_general is the crime level code, not the num of occurences
    # TODO: Fix the method of calculating a level
    if len(crimes) == 0:
        return 0 # avoid division by zero
    total = sum([int(crime['ucr_general']) / 100 for crime in crimes])
    return total / len(crimes)
        
