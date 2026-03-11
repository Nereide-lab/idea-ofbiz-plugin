Map result = [:]
result = run service: 'makeHorse', with: [:]
result = run service: 'makeWitcher', with: [:]
result = run service: 'makeGame', with: [:]
def foo = result.<caret>
result = run service: 'makeHorse', with: [:]
