#!/bin/bash

git config --global alias.rebase-last-five '!b="$(git branch --no-color | cut -c3-)"
h="$(git rev-parse $b)"
echo "Current branch: $b $h"
c="$(git rev-parse $b~4)"
echo "Recreating $b branch with initial commit $c ..."
git checkout --orphan new-start $c
git commit -C $c
git rebase --onto new-start $c $b
git branch -d new-start
git gc'
