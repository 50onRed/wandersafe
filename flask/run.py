#!/usr/bin/env python
from server.app import create_app
app = create_app()
app.run(debug=True)
