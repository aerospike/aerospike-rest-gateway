# Build Directions

To build the app, from this directory run `gradle war` The name of the resulting `.war` file is determined by the fields in`build.gradle`

The resulting file will be placed in `build/libs/`.

It is also possible to run the app via `gradle bootRun`. This will simply run the application with an embedded tomcat server, no packaging will be done.
