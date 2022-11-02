// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy.
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp
//    do widelców. Wyniki przedstaw na wykresach.

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb) {
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.

    const fork = this;
    let bebAlgorithm = function (time) {
        setTimeout(function () {
            if (fork.state === 0) {
                fork.state = 1;
                cb();
            } else {
                if (time < 4096) // max for 2^12 (better execution with big N and philosophersNum)
                    bebAlgorithm(time * 2);
                else
                    bebAlgorithm(time);
            }
        }, time);
    };

    bebAlgorithm(1);
}

Fork.prototype.release = function() {
    this.state = 0;
}

var Conductor = function(philosophersNum) {
    this.accessNum = philosophersNum - 1;
    return this;
}

Conductor.prototype.canAccess = function (){
    return (this.accessNum > 0);
}

Conductor.prototype.waitInQueue = function (cb){
    const conductor = this;
    let conductorBebAlgorithm = function (time) {
        setTimeout(function () {
            if (conductor.canAccess()) {
                conductor.accessNum -= 1;
                cb();
            } else {
                if (time < 4096) // max for 2^12 (better execution with big N and philosophersNum)
                    conductorBebAlgorithm(time * 2);
                else
                    conductorBebAlgorithm(time);
            }
        }, time);
    };

    conductorBebAlgorithm(1);
}

Conductor.prototype.releaseAccess = function (){
    this.accessNum += 1;
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.acquireTime = 0;
    this.startTime = 0;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    let forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        philosopher = this;

    // zaimplementuj rozwiązanie naiwne
        // każdy filozof powinien 'count' razy wykonywać cykl
        // podnoszenia widelców -- jedzenia -- zwalniania widelców

    let loopNaive = function (count) {
        setTimeout(function () {
            if (count === 0) {
                philosophersRunningCount--;
                if (philosophersRunningCount === 0){
                    printAvgTimes();
                }
                return;
            }

            philosopher.startTime = new Date().getTime();
            forks[f1].acquire(function () {
                if (debugPrints)
                    console.log("philosopher " + id + " acquired left fork");
                forks[f2].acquire(function () {
                    philosopher.acquireTime += new Date().getTime() - philosopher.startTime;
                    if (debugPrints)
                        console.log("philosopher " + id + " acquired right fork and start to eat");
                    setTimeout(function () {
                        forks[f1].release();
                        forks[f2].release();
                        if (debugPrints)
                            console.log("philosopher " + id + " released both forks");
                        loopNaive(count - 1);
                    }, Math.floor(Math.random() * 10)) // random as an eating time
                })
            })
        }, Math.floor(Math.random() * 100)); // random as a thinking time (bigger than eating time)
    }

    loopNaive(count);
}

Philosopher.prototype.startAsym = function(count) {
    let forks = this.forks,
        id = this.id,
        f1 = (id % 2 === 1) ? this.f2 : this.f1,
        f2 = (id % 2 === 1) ? this.f1 : this.f2,
        philosopher = this;

    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    let loopAsym = function (count) {
        setTimeout(function () {
            if (count === 0) {
                philosophersRunningCount--;
                if (philosophersRunningCount === 0){
                    printAvgTimes();
                }
                return;
            }

            philosopher.startTime = new Date().getTime();
            forks[f1].acquire(function () {
                if (debugPrints)
                    console.log("philosopher " + id + " acquired " + ((id % 2 === 1) ? "right" : "left") + " fork");
                forks[f2].acquire(function () {
                    philosopher.acquireTime += new Date().getTime() - philosopher.startTime;
                    if (debugPrints)
                        console.log("philosopher " + id + " acquired " + ((id % 2 === 1) ? "left" : "right") + " fork and start to eat");
                    setTimeout(function () {
                        forks[f1].release();
                        forks[f2].release();
                        if (debugPrints)
                            console.log("philosopher " + id + " released both forks");
                        loopAsym(count - 1);
                    }, Math.floor(Math.random() * 10)) // random as an eating time
                })
            })
        }, Math.floor(Math.random() * 100)); // random as a thinking time (bigger than eating time)
    }

    loopAsym(count);
}

Philosopher.prototype.startConductor = function(count, conductor) {
    let forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        philosopher = this;

        // zaimplementuj rozwiązanie z kelnerem
        // każdy filozof powinien 'count' razy wykonywać cykl
        // podnoszenia widelców -- jedzenia -- zwalniania widelców

    let loopConductor = function (count) {
        setTimeout(function () {
            if (count === 0) {
                philosophersRunningCount--;
                if (philosophersRunningCount === 0){
                    printAvgTimes();
                }
                return;
            }

            philosopher.startTime = new Date().getTime();
            conductor.waitInQueue(function() {
                forks[f1].acquire(function () {
                    if (debugPrints)
                        console.log("philosopher " + id + " acquired left fork");
                    forks[f2].acquire(function () {
                        philosopher.acquireTime += new Date().getTime() - philosopher.startTime;
                        if (debugPrints)
                            console.log("philosopher " + id + " acquired right and start to eat");
                        setTimeout(function () {
                            forks[f1].release();
                            forks[f2].release();
                            if (debugPrints)
                                console.log("philosopher " + id + " released both forks");
                            conductor.releaseAccess();
                            loopConductor(count - 1);
                        }, Math.floor(Math.random() * 10)) // random as an eating time
                    })
                })
            })
        }, Math.floor(Math.random() * 100)); // random as a thinking time (bigger than eating time)
    }

    loopConductor(count);
}

// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców,
// a nie każdego z osobna

Philosopher.prototype.acquireBothForks = function (cb){
    const fork1 = this.forks[this.f1],
          fork2 = this.forks[this.f2];

    let bebAlgorithm = function (time) {
        setTimeout(function () {
            if (fork1.state === 0 && fork2.state === 0) {
                fork1.state = 1;
                fork2.state = 1;
                cb();
            } else {
                if (time < 4096) // max for 2^12 (better execution with big N and philosophersNum)
                    bebAlgorithm(time * 2);
                else
                    bebAlgorithm(time);
            }
        }, time);
    };

    bebAlgorithm(1);
}
Philosopher.prototype.startBothForks = function (count) {
    const philosopher = this;

    let loopBothForks = function (count) {
        setTimeout(function () {
            if (count === 0) {
                philosophersRunningCount--;
                if (philosophersRunningCount === 0){
                    printAvgTimes();
                }
                return;
            }

            philosopher.startTime = new Date().getTime();
            philosopher.acquireBothForks(function (){
                philosopher.acquireTime += new Date().getTime() - philosopher.startTime;
                if (debugPrints)
                    console.log("philosopher " + philosopher.id + " acquired both forks");
                setTimeout(function () {
                    philosopher.forks[philosopher.f1].release();
                    philosopher.forks[philosopher.f2].release();
                    if (debugPrints)
                        console.log("philosopher " + philosopher.id + " released both forks");
                    loopBothForks(count - 1);
                }, Math.floor(Math.random() * 10)) // random as an eating time
            })
        }, Math.floor(Math.random() * 100)); // random as a thinking time (bigger than eating time)
    }

    loopBothForks(count);
}


const args = process.argv.slice(2);
var N = parseInt(args[0]);
var eatNum = parseInt(args[1]);
var loopVersion = args[2];
var debugPrints = (args.length > 3)
var forks = [];
var philosophers = []
let conductor = new Conductor(N)
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

function printAvgTimes (){
    for (var i = 0; i < N; i++) {
        console.log("PHILOSOPHER " + philosophers[i].id + " AVG ACQUIRE TIME (ms): " + philosophers[i].acquireTime / eatNum);
    }
}

let philosophersRunningCount = N;
for (var i = 0; i < N; i++) {
    if (loopVersion === "naive")
        philosophers[i].startNaive(eatNum);
    else if (loopVersion === 'asym')
        philosophers[i].startAsym(eatNum);
    else if (loopVersion === 'bothForks')
        philosophers[i].startBothForks(eatNum);
    else if (loopVersion === 'conductor')
        philosophers[i].startConductor(eatNum, conductor);
}



