# rates_stream_api
This is a generic api to connect to multiple product rate providers and maintain a store of recent quotes.

Its implemented only for FX rates at the moment. On startup the api registers a thread which manages connection with a 3rd party rates provider and maintains an upto date copy of the rates. The thread continously monitors the heartbeat of the rate stream and tries to auto connect in the event of a failure.
