## From callback based API to Promises and finally to `async` iterator

This repository provides 3 tags for each development phase of the domain model:

1. callbacks based API
2. association from `Playlist` to `Track` with `Promise<Array<Track>>`
3. association from `Playlist` to `Track` with `AsyncIterable<Track>>`

The last implementation on point 3. takes advantage of asynchronous iterators 
of [ECMAScript 2018](https://www.ecma-international.org/ecma-262/9.0/#sec-symbol.asynciterator).

Get started:

```
npm install
npm run test
```

To check coverage run: `jest --coverage`