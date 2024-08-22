// server.js  
const express = require('express');  
const path = require('path');  
const https = require('https')
const fs = require('fs')

const app = express();  
const PORT = 3000;  

const options = {
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.cert')
}

// 提供静态文件  
app.use(express.static(path.join(__dirname)));  

// 路由处理  
app.get('/', (req, res) => {  
    res.sendFile(path.join(__dirname, 'index.html'));  
});  

// 启动服务器  
https.createServer(options, app).listen(PORT, () => {  
    console.log(`Server is running at https://localhost:${PORT}`);  
});