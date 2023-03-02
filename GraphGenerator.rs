#![allow(unused)]

use num_integer::Integer;
use petgraph::visit::IntoNeighbors;
use petgraph::{
    adj::NodeIndex,
    dot::{Config, Dot},
    graph::UnGraph,
};

fn main() {
    let chromatic_number = 5;
    let num_nodes = 150;
    let avg_edges_per_node = 20; // The average degree of the nodes
    let num_edges = num_nodes * avg_edges_per_node;
    let b_vec = vec![10, 21, 12, 12, 12];
    // Values taken from Leighton
    let modulus =84_035 ;
    let increment = 6_859;
    let mut x_i = vec![36_876];
    let multiplier = 8_401;
    let graph = gen_graph_with_known_chromatic(
        chromatic_number,
        num_nodes,
        num_edges,
        b_vec,
        modulus,
        increment,
        multiplier,
        x_i,
    );
}
#[derive(Debug)]
pub struct Edge {}
#[derive(Debug)]
pub struct Node {}

/// From:
/// Leighton, F. T. (1979). A graph coloring algorithm for large scheduling problems.
/// In Journal of Research of the National Bureau of Standards (pp. 489-506).
/// http://doi.org/10.6028/jres.084.024
pub fn gen_graph_with_known_chromatic(
    k: usize,
    num_nodes: usize,
    num_edges: usize,
    b_vec: Vec<u32>,
    modulus: usize,
    increment: usize,
    multiplier: usize,
    mut x_i: Vec<usize>,
) -> UnGraph<Node, Edge> {
    // See https://en.wikipedia.org/wiki/Linear_congruential_generator
    assert!(b_vec.len() == k, "b_vec.len() == {} != {k}", b_vec.len());
    assert!(b_vec[k - 1] >= 1, "b_vec[k-1] == {} >= 1", b_vec[k - 1]);
    println!("Values: m={modulus}, n={num_nodes}, k={k}, c={increment}, a={multiplier}, b_vec: {b_vec:?}");

    // Require that k divides n
    assert!(num_nodes % k == 0);
    // 2. gcd(m, num_nodes) == k
    assert!(modulus.gcd(&num_nodes) == k);
    assert!(modulus.gcd(&increment) == 1);
    assert!(0 < increment && increment < modulus);

    assert!(0 < multiplier && multiplier < modulus);

    // Finding a random sequence in [0, m-1] via Knuth's linear conguential method
    assert!(0 < x_i[0] && x_i[0] < modulus);
    let num_rand_numbers = 8000;
    for i in (1..num_rand_numbers) {
        // x[i] = (a * x[i-1] + c) % m
        x_i.push((x_i[i - 1] * multiplier + increment) % modulus);
    }

    // Construct y_i in [0, n-1] such that y_i = x_i % n
    let y_i = x_i.iter().map(|x| x % num_nodes).collect::<Vec<_>>();

    let mut graph = UnGraph::new_undirected();
    // Now add `num_nodes` nodes and connect them in a line so that we
    // don't have any island nodes
    let mut prev;
    let mut curr = graph.add_node(Node {});
    // graph.node_weight_mut(curr).unwrap().node_idx = Some(curr);
    for n in 1..num_nodes {
        prev = curr;
        curr = graph.add_node(Node {});
        // graph.node_weight_mut(curr).unwrap().node_idx = Some(curr);
        graph.add_edge(prev, curr, Edge {});
    }

    // Cache the node indices so we don't have to look them up every time
    let node_indices = graph.node_indices().collect::<Vec<_>>();

    // Define an h-clique as a set of nodes which are all fully connected to
    // each other, such that each node has h-1 edges coming from it.
    // This corresponds to h colours being required to colour that clique.
    let mut y_idx = 0;
    // Create multiple cliques of decreasing degrees: start with the
    // high-degree cliques and looping until you link up the
    // cliques of degree 2.
    'make_cliques: for (num_cliques, clique_degree) in
        b_vec.into_iter().zip((1..(k + 1)).into_iter()).rev()
    {
        println!("Making {num_cliques} cliques of degree {clique_degree}");
        if clique_degree <= 1 {
            break;
        }
        for clique_idx in 0..num_cliques {
            // Create a clique with nodes indexed y_idx to y_idx+clique_degree
            for i in 0..clique_degree {
                for j in (i + 1)..clique_degree {
                    let a = node_indices[y_i[y_idx + i]];
                    let b = node_indices[y_i[y_idx + j]];
                    // If we've already added this edge => continue
                    if graph.contains_edge(a, b) {
                        continue;
                    }
                    // If either of the candidate nodes already have
                    // the maximum number of neighbours => continue
                    if graph.neighbors(b).count() == k || graph.neighbors(a).count() == k {
                        continue;
                    }
                    // Now actually add the edge
                    graph.add_edge(a, b, Edge {});
                    // If we've reached the maximum number of edges
                    // => stop adding edges
                    if graph.edge_count() == num_edges {
                        print!("Have enough edges, exiting");
                        break 'make_cliques;
                    }
                }
            }
            y_idx += clique_degree;
        }
    }
    let dotstring = Dot::with_config(&graph, &[Config::EdgeNoLabel]);
    println!("Dotstring of graph:\n{:?}", dotstring);
    return graph;
}
