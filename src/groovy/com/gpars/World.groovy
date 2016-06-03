package com.gpars

class World {
    int alive = 100000
    int undead = 10

    public void eatBrains() {
        alive = alive - undead
        undead = undead * 2 //When Zombie eats brain then they all gets doubled and that many humans are dead
        if (alive <= 0) {
            alive = 0
            println "ZOMBIE APOCALYPSE!"
        }
    }

    public void shotgun() {
        undead = undead * 0.95   // WHen shotgun is fired the zombies gets reduced by 5%
    }

    public boolean apocalypse() {
        alive <= 0
    }

    public void report() {
        if (alive > 0) {
            println "alive=" + alive + " undead=" + undead
        }
    }
}
