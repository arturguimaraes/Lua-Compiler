function soma(a, b, c)
  local soma = 0
  soma = a + b + c
  return soma
end

local num1, num2, num3, soma = 0, 0, 0, 0
num1 = 2
num2 = 5
num3 = 10
soma = soma(num1, num2, num3)
print("Soma: ", soma)