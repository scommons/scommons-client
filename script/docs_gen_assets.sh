#!/bin/sh

set -e

sbt "project scommons-client-showcase" clean fullOptJS::webpack
cp "showcase/target/scala-2.12/scalajs-bundler/main/styles/scommons-client-showcase-opt.css" "docs/showcase/assets/styles/scommons-client-showcase-opt.css"
cp "showcase/target/scala-2.12/scalajs-bundler/main/scommons-client-showcase-opt-library.js" "docs/showcase/assets/scommons-client-showcase-opt-library.js"
cp "showcase/target/scala-2.12/scalajs-bundler/main/scommons-client-showcase-opt-loader.js" "docs/showcase/assets/scommons-client-showcase-opt-loader.js"
cp "showcase/target/scala-2.12/scalajs-bundler/main/scommons-client-showcase-opt.js" "docs/showcase/assets/scommons-client-showcase-opt.js"

cp -r "assets/src/main/public/" "docs/showcase/assets/lib/scommons-client-assets/"
