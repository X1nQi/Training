# 我需要做到的是什么？

我需要做一个HTTP服务器，从宏观来看我需要实现的最主要的功能是什么？

> 接收客户端的http请求，解析并返回资源和信息给客户端

# 在第一个版本我要做到什么？

在一个版本中，我不考虑使用socket与多线程，我会读取我硬盘中的文件，文件的内容写了HTTP请求内容，包含完整的请求行、请求头、空行、请求体

**目标：当解析完httpRequest对象后，在本机输出“xxx解析完成”，并输出解析的内容**

1. 我会通过字符流FileRead去读取文件，获取FileRead对象，然后再使用字节缓冲流BufferedRead读取FileRead对象
2. 我会使用Stringbuilder去存储http请求的原始内容
3. 创建httpRequest对象
4. 调用三个方法，分别是：解析请求行、解析请求头、解析请求体，将相应的属性写入httpRequest

> 第一版于2024年4月24日19:58分编写完毕

# 在第二个版本我要做到什么？

现在已经实现了第一个版本，解析HTTP请求，在第二个版本中，我要完成响应HTTP请求，此版本任然使用本地文件作为HTTP请求来源

**目标：当接收到了HTTP请求时，在本机输出Hello**

1. 定义好响应体对象，包括响应行、响应头、响应体
2. 定义好生成响应体的方法

> 2024年4月24日21:00:33完成第二版本的编写

# 在第三个版本我要做到什么？

第二个版本的生成响应体方法，是不太优雅；纯纯是获取数据并输出在本地，在第三个版本中要使用socket启动服务进行监听，并在本机控制台打印出HTTP请求

目标：从浏览器访问localhost:9090,本机打印请求体的内容

1. 使用Scoket监听9090端口
2. 将本地文件IO替换为字节流IO

> 2024年4月24日21:57:34完成第三版本的编写

## 遇到的困难

http发送了，但是请求头是null

问题原因：http请求可能有请求体，也可能没有请求体；在处理请求头的代码中，我只写了判断有请求体时的代码，导致第22行的代码不会被运行，在第26加上了设置请求头的语句，在两种情况下都会设置请求头；只不过在有请求体的情况下，请求体会被设置两次，不太优雅。

```java
private static void decodeRequestHeader(StringBuilder httpRcontent,Request request) throws IOException{
        //创建一个Map对象，用于存储请求头的信息,并初始化容量
        HashMap<String,String> headers = new HashMap<>(16);
        //获取请求的原始数据，解析为字符串数组
        String[] httpContent = httpRcontent.toString().split("\n");

        //读取请求头信息，每行都是一个键值对，以空行作为请求头的标准
        String[]kv;//键值对key:values
        int tag=0;
        for(String line:httpContent){
            line = line.trim();//去除字符串两端的空格
            //判断是否为请求头信息
            tag++;
            if (tag == 1){continue;}

            if(!line.equals("")){
                kv = line.split(":");
                headers.put(kv[0],kv[1]);

            }else {//请求有请求体的情况
                //设置请求对象中的请求头信息
                request.setHeaders(headers);
                break;
            }
            //请求没有请求体的情况
            request.setHeaders(headers);
        } //for end
        System.out.println("请求头解析完成");
        System.out.println(request.getHeaders());
    }
```

