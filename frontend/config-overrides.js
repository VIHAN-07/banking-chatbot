const path = require('path');

module.exports = function override(config, env) {
  // Disable source maps in development for faster builds
  if (env === 'development') {
    config.devtool = false;
    
    // Optimize webpack for faster builds
    config.optimization = {
      ...config.optimization,
      removeAvailableModules: false,
      removeEmptyChunks: false,
      splitChunks: false,
    };

    // Reduce bundle analysis
    config.resolve.symlinks = false;
    
    // Cache webpack compilation
    config.cache = {
      type: 'filesystem',
      cacheDirectory: path.resolve(__dirname, '.webpack-cache'),
    };
  }

  return config;
};
