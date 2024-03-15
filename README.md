# Sphere Renderer

## Overview

Sphere Renderer is an Android application designed to showcase the capabilities of OpenGL ES 2.0 by rendering a dynamically generated, colored 3D sphere. This application demonstrates essential concepts such as OpenGL ES setup, shader programming, geometry generation, and basic animation within the Android platform.

## Features

- **Dynamic Sphere Generation**: Creates a 3D sphere based on specified parameters such as radius, number of stacks, and slices, allowing for detailed customization of the sphere's appearance.
- **Shader-Based Rendering**: Utilizes vertex and fragment shaders to render the sphere, demonstrating the power and flexibility of GPU programming.
- **Interactive Color Changes**: Changes the color of the sphere every 10 seconds to a random color, illustrating dynamic uniform updates in OpenGL ES.
- **3D Viewing Experience**: Implements basic 3D camera functionality, providing a foundational example of how to handle 3D perspectives and view transformations.

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or higher
- Android SDK with OpenGL ES 2.0 support
- An Android device or emulator capable of running applications targeting API level 23 or higher

### Installation

1. Clone the repository to your local machine.

```bash
git clone https://github.com/angatiabenson/sphere-renderer
```
2. Open Android Studio and select "Open an existing Android Studio project". Navigate to the project's root directory and click OK.
3. Connect your Android device to your computer or ensure your emulator is set up.
4. Click on the "Run" button in Android Studio to build and install the application on your device or emulator.

## Usage

Upon launching the Sphere Renderer app, you will see a rotating 3D sphere rendered on your screen. The sphere will automatically change its color every 10 seconds. This application serves as a demonstration and does not include interactive user controls beyond what is provided by the Android operating system for managing applications.

## Architecture

The application is structured around three main Java classes:

- `MainActivity.java`: Initializes the application's main window, setting up the `GLSurfaceView` for rendering.
- `MainRenderer.java`: Implements the `GLSurfaceView.Renderer` interface, handling OpenGL ES initialization, shader compilation, and rendering logic.
- `Sphere.java`: Represents the 3D sphere, responsible for generating the sphere's geometry and performing rendering operations.

## Customization

To customize the sphere's appearance, modify the parameters passed to the `createSphereCoords` method in the `Sphere` class. Adjusting the radius, number of stacks, and number of slices will change the size and level of detail of the sphere.

## License

Sphere Renderer is open-source software licensed under the MIT License. See the LICENSE file for more details.
