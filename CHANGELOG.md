### Version 2.2.4:

-> Fixed various issues with the Aggressive Spawner not invoking all the possible entries.

-> Reimplemented the Teleporting Manager:

- This new system better handles edge cases, and it is much better and faster.

- The new system also removes issues with the teleporting data being corrupted. 
Note, however that the new data logic is incompatible with the old one, so you must start from the beginning...

-> Updated to BML 1.0.19 and the base build logic.

-> Removed the Operations Tasker thread - this should optimize the experience for most players (And the server as well).