const express = require('express');
const app = express();
const cors = require('cors');
const Routes = require('./routes/routes');

app.use(express.json());
app.use(cors());

app.use('/api', Routes);

app.disable('x-powered-by');

app.get('/api', (req, res) => {
  try {
    res.status(200).json({
      status: 'success',
      data: [],
      message: 'Welcome to API Page',
    });
  } catch (e) {
    res.status(500).json({
      status: 'error',
      message: 'Internal Server Error',
    })
  }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port http://localhost:${PORT}`);
});
