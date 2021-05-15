javafx-dao-boardgame
==========================

Dao board game implementation in [JavaFX](https://openjfx.io/).

Description of the game:

Board game with two players, played on a four by four grid, with two different
colors of disks. The players take turns moving their respective colors.
A disk can be moved vertically, horizontally or diagonally, but they can
only step on empty squares and cannot step over other discs.

A player wins if they manage to move all four of their discs in the same column or row,
if they place their discs in a two by two square, or in the four corners of the grid.

A player loses the game if they have three of their discs adjacent to a disk of the
other player which is in a corner of the grid. This rule makes sure that restricting
one of the disks and prolonging the match is not possible.