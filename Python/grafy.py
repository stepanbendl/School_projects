graph1 = {
    "A": ["B","C"],
    "B": ["D","E"],
    "C": ["E","D"],
    "D": ["E"],
    "E": ["A"]
}


def recursive_dfs(graph, start, visited=[]):
    visited.append(start)
    for item in graph[start]:
        if item not in visited:
            recursive_dfs(graph, item)
    return visited


def iterative_dfs(graph, start):
    visited = []
    stack = [start]
    while stack:
        item = stack.pop(0)
        if item not in visited:
            visited.append(item)
            stack = graph[item] + stack
    return visited


def iterative_bfs(graph, start):
    visited = []
    stack = [start]
    while stack:
        item = stack.pop(0)
        if item not in visited:
            visited.append(item)
            stack += graph[item]
    return visited


print(recursive_dfs(graph1, "A"))
print(iterative_dfs(graph1, "A"))
print(iterative_bfs(graph1, "A"))

print(float('inf') > 10000000000000000)
