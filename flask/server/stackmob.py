import requests

def get_crime_near(lat, lon, meters):
    radians = meters / 6367500.0

    headers = {
            'Accept': 'application/vnd.stackmob+json; version=0',
            'X-StackMob-API-Key': '9f08750f-14b1-4e37-9867-9c54d7f4f0d5'
            }

    params = {
            'location[near]': '{0},{1},{2}'.format(lat, lon, radians)
            }

    response = requests.get('http://api.stackmob.com/crime', headers=headers, params=params)

    return response.json()
