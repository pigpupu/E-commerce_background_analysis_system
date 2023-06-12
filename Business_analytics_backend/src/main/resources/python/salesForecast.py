# 干脆直接使用线性拟合
# 数据处理
# 最开始的时间为 2016-9-1
from sys import argv
with open(argv[1]) as f:
  line = f.read()
a=line.split('\n')
x = a[0].split()
y = a[1].split()
# 转为int型数组
for i in range(len(x)):
  x[i]=int(x[i])
for i in range(len(y)):
  y[i]=int(y[i])

from scipy import optimize

def f_1(x, A, B):
  return A * x + B

# 拟合点
x0 = x
y0 = y


# 直线拟合与绘制
A1, B1 = optimize.curve_fit(f_1, x0, y0)[0]

# 通过打印的方式返回
print(A1)
print(B1)

