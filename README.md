# Playground renovations
2022 Application Task by Herkus Šimėnas

## Comments on design choices used in the task

### Playsite > Playground
Changed the name *Playsite* to *Playground* in the task. I do not think "Playsite" is a word.

### Queue skipping algorithm
The second playground renovation update had this sentence:
> up to 3 kids cannot be skipped by VIPs twice

I found that this might be interpreted in a few ways.
Only realised this in the later stages of writing the code for the task, usually I would have just asked for what is the correct interpretation of this.

#### The three ways I found of interpreting this:
Let's begin with:  
KKK + V ⟶ VKKK (The kid with VIP privileges skips the two kids in the queue)  
VKKK - V ⟶ KKK (The VIP kid is allowed into the playground, so he leaves the queue)  
KKK + V ⟶ KKKV (The new kid with VIP is added to the end of the queue, since the two kids in front now can no longer be skipped by him)  
KKKV + KKK ⟶ KKKVKKK  
KKKVKKK + VV ⟶ ?

#### Here I can see three outcomes:
1. KKKVVKKKV  
The last 3 simple kids were never skipped by any VIPs, so the first VIP is able to skip all three of them and the second one can no longer skip all three of them. **(This was how I interpreted this task and how it was coded)**
2. KKKVVVKKK  
Only up to 3 kids cannot be skipped by VIPs at one time. The new VIPs would only skip up to the 3 who already were skipped. But every other simple kid can be skipped however many times as long as there are three who have been skipped once in the front of the queue.
3. KKKVKKKVV  
The spacing between VIPs is always 3 simple kids if possible.

### Percentage
> measured in % of

Since percentage was mentioned and Basis point calculations or decimals were not mentioned, I chose to use `int` as the type for returning the percentage.
Although it would be easy to change the implementation to use `double` or `String`.

### Remove/add kids from/to Double Swing Playground
* The removal was implemented so that if there would be only child remaining at the playground and nobody was waiting in the waiting queue, he himself would be moved to the front of the waiting queue and his visit would be stopped since he cannot use the swings alone.
* The addition was implemented so that if there was a kid waiting in the queue at the time of the new kid arriving, they both start their visit and are moved to the playing list.

### Abstract Playground
Decided on removing the IPlayside interface.  
First of all the naming is bad, The prefix "I-" should be avoided.
> Who knows what other restrictions EU may impose on existing or future playsite types...

Playground was made abstract in order for the code to be more expandable and easier to maintain.  
All the types of playgrounds got their own classes that extend the abstract one since the quoted sentence would suggest that more logic between the different types of playgrounds could differ in the future, while still maintaining some common things between them.


### Testing
I think each test should have separate logic and the `setup()` method was not necessary in this case.  
* Fixed the naming of tests.
* Added tests for different types of Playgrounds regulations.

