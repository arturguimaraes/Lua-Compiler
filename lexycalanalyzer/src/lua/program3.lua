function soma(a, b)
  local soma = 0
  soma = a + b
  return soma
end

local num1, num2, soma = 0, 0, 0
num1 = 2
num2 = 5
soma = soma(num1, num2)
print("Soma: ", soma)