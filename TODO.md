Backend
- Improve MongoDB codecs with reader/writer methods
- Externalize config into separate /config folder which does not get committed to repo (.gitignore)
- Write README.md with instructions
- 
- Build v1 API:
  - GET /v1/instances/                     Return full list of instances (includes stats)
  - GET /v1/instances/<instanceId>         Return single instance (includes stats)
  - POST /v1/instances                     Submit a new instance for addition
  - GET /v1/instances/stats/<instanceId>   
- Create website
  - Connect to v1 API and display values

Deploy server on AWS
  - ECS

Geolocation API:
- Create interface for looking up data. 
- Switch to ip-api.com (45 reqs/minute for free)
- Check qualitiy first
