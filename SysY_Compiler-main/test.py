# -*- coding: utf-8 -*-
"""
Created on Sat Oct 16 20:35:03 2021

@author: 16922
"""


def loadTestFiles(n, testType = 'A'):
    testfile = 'C:/Users/16922/Desktop/helptest/testfiles/' + testType + '/' + 'testfile' + str(n) + '.txt'
    inputfile = 'C:/Users/16922/Desktop/helptest/testfiles/' + testType + '/' + 'input' + str(n) + '.txt'
    answer = 'C:/Users/16922/Desktop/helptest/testfiles/' + testType + '/' + 'output' + str(n) + '.txt'
    
    testfile = open(testfile, 'r')
    inputfile = open(inputfile, 'r')
    answer = open(answer, 'r')
    
    mytestfile = open('testfile.txt', 'w')
    myinput = open('input.txt', 'w')
    myoutput = open('standard.txt', 'w')
    
    for line in testfile:
        mytestfile.write(line)
    for line in inputfile:
        myinput.write(line)
    for line in answer:
        myoutput.write(line)
        
    testfile.close()
    inputfile.close()
    answer.close()
    
    mytestfile.close()
    myinput.close()
    myoutput.close()
    
def conductTest(n = 1, testType = 'A'):
    print('Testing file ' + testType + '--' + str(n))
    
    # os.system("javac .\\C:\\Code\\JavaCode\\GrammarAnalyse\\src\\Compiler.java")
    # os.system("java C:\\Code\\JavaCode\\GrammarAnalyse\\src\\Compiler")
    
    f_myoutput = open('pcoderesult.txt', 'r')
    f_standard = open('standard.txt', 'r')
    
    my = []
    for line in f_myoutput:
        line = line.strip('\n')
        my.append(line)
        
    std = []
    for line in f_standard:
        line = line.strip('\n')
        std.append(line)
    
    print("length of me :", len(my))
    print("length of std:", len(std))
    
    flag = True
    for i in range(0, min(len(my), len(std))):
        if(my[i] != std[i]):
            print("different at : ", i)
            flag = False
            break
    if (len(my) != len(std)):
        print("\nwhat a pity...failed")
    elif flag:
        print('\nCongratulations!!! at testfile ' + testType + '--' + str(n))
    else:
        print("\nwhat a pity...failed")
    f_myoutput.close()
    f_standard.close()
    return flag
    
if __name__ == '__main__':
    '''
    for testType in ['A', 'B', 'C']:
        for n in range(1, 31):
            loadTestFiles(n, testType)
            flag = conductTest(n, testType)
            if flag == False:
                break
        if flag == False:
            break
    print("All Done")
    '''
    loadTestFiles(21, 'A')
    conductTest(21, 'A')
    