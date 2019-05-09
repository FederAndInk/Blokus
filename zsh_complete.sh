#!/usr/bin/env bash

echo "
# Blokus dev test completion
export fpath=($(realpath .zsh_completion) \$fpath)
compdef _blokus_test blokus_test
autoload -U _blokus_test" >>~/.zshrc

exec zsh
