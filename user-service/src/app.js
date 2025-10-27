const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const userRoutes = require('./routes/userRoutes');
const { sequelize } = require('./models');

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(express.json());

// Routes
app.use('/api/users', userRoutes);

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    message: 'Welcome to the Bookstore User Service',
    endpoints: {
      health: 'GET /health',
      register: 'POST /api/users/register',
      login: 'POST /api/users/login',
      profile: 'GET /api/users/profile',
      updateProfile: 'PUT /api/users/profile'
    }
  });
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'UP' });
});

// Database connection and server start
async function startServer() {
  try {
    console.log('Starting server setup...');
    console.log('Environment:', {
      NODE_ENV: process.env.NODE_ENV,
      PORT: process.env.PORT,
      DB_HOST: process.env.DB_HOST,
      DB_NAME: process.env.DB_NAME
    });

    console.log('Attempting to connect to database...');
    await sequelize.authenticate();
    console.log('Database connection established successfully.');
    
    const server = app.listen(PORT, '0.0.0.0', () => {
      console.log(`User service running on port ${PORT}`);
      console.log('Available endpoints:');
      console.log('- GET / (Welcome page)');
      console.log('- GET /health');
      console.log('- POST /api/users/register');
      console.log('- POST /api/users/login');
    });

    // Handle server errors
    server.on('error', (error) => {
      console.error('Server error:', error);
      if (error.code === 'EADDRINUSE') {
        console.error(`Port ${PORT} is already in use`);
      }
    });

    // Handle process termination
    process.on('SIGTERM', () => {
      console.log('Received SIGTERM. Closing server...');
      server.close();
    });

  } catch (error) {
    console.error('Server startup error:', error);
    console.error('Database connection details:', {
      host: process.env.DB_HOST,
      port: 3306,
      database: process.env.DB_NAME,
      user: process.env.DB_USER
    });
    process.exit(1);
  }
}

startServer();