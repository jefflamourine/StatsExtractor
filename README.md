# Hockey? Stats Tracker

A tool to track and output the stats from a Hockey? game.

##### Output: 
- Files w/ list of goals from the game and per player performances
- Upload to [Hockey? Web App] (http://github.com/jefflamourine/hockey) at <https://hockey-jefflamourine.rhcloud.com>

##### Compatibility:
- Currently only compatible with Hockey? .55f
- Adaptation to other client versions only requires changing memory addresses
- Planned future support for dedicated servers.

##### Use:
- Run Hockey? client version .55f
- Enter game to be tracked
- Begin tracking game by typing start command in in-game chat

##### Things that could break: [C] = Current state, [P] = Proposed improvement:
- [C] Running tracker before Hockey? prompts user to start the game and press enter
- [C] Tracker only records events AFTER it started if the game had already been started.
- [C] Start command is given bad parameters: 1) Wrong number of args -> ignored, 2) Game verification fails -> ignored, 3) Else -> names are just whatever was given
- [C] If player names don't match when the stats are uploaded, the request will fail, the database won't update at all, but payload of the request should be saved on the server, and the local files will still be created.

