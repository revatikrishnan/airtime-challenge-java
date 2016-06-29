"# airtime-challenge-java" 
This is the solution to challenge :

http://challenge2.airtime.com:7182/

You are a maintenance worker of a cyberspace labyrinth, tasked with creating a report of all the rooms in the labyrinth where the lights are no longer functional.  The labyrinth has the following HTTP Interface:

(all requests must contain the header X-Labyrinth-Email: <your email address>)

GET /start
// This tells you the room that you start in.
returns {
  roomId: '<roomId of first room>'
};

GET /exits?roomId=<roomId>
// This allows you to see which exits are available for this current room.
returns {
  exits: ['north', 'south', 'east', 'west']
}

GET /move?roomId=<roomId>&exit=<exit>
// This allows you to see what the roomId is through an exit.
returns {
  roomId: '<roomId of room connected by exit>'
}

GET /wall?roomId=<roomId>
// This allows you to see what the writing is on the wall for a particular room if the lights are working.
returns {
   writing: '<string>'
   order: <number>
}

// If the lights aren't working
returns {
  writing: 'xx'
  order: -1
}

POST /report
// Submit your maintenance report to the mothership. Because the mothership knows that some workers are lazy and untruthful, the mothership requires a challenge code that is made by concatenating all the 'writing' on the walls in lit rooms, in the order designated by 'order' from lowest to greatest.

body {
  roomIds: [array of room ids whose lights were broken],
  challenge: 'challenge code'
}

Note the /report expects a JSON-formatted post body.

The next steps will be apparent once the mothership approves your maintenance report.

Hint: If you get a 404, you probably are doing something wrong.

Guidance on your solution:
* First and foremost your code should generate the correct report.
* We should be able to run your solution without much trouble (e.g. set up a package.json or gulp file if you use node.js)
* Include a readme file of some sort that details how to run your solution and any design descisions.
* You should demonstrate more than just the ability to solve the problem.  In the end we need to see how you write software professionally.  This means writing clean, easy to follow, appropriately commented code that you would expect to see in a production environment.  Demonstrate to us the quality you are committed to every day.
