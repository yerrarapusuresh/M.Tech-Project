{-
Generate random planar graphs using Haskell and QuickCheck.  
A variant of this program was used to automatically generate 
input test cases for a problem of the 2012 TIUP programming contest 
(http://caos.di.uminho.pt/~tiup/).

Pedro Vasconcelos, 2012
-}
module Graph where

import Data.List (union,insert,delete,nub)
import Data.Set(Set)
import qualified Data.Set as Set
import Data.Map(Map, (!))
import qualified Data.Map as Map
import Test.QuickCheck  -- for random test-case generation
import Control.Monad (liftM)
import System.Cmd (system)


-- Graphs are maps from nodes to adjacency lists.
-- Directed edges are pairs of nodes.
type Graph a = Map a [a]
type Edge a = (a,a)

-- Make an undirected graph from a list of edges;
-- for each edge (v,v') adds both (v,v') and (v',v).
makeGraph :: Ord a => [Edge a] -> Graph a
makeGraph arcs = Map.fromListWith union $ 
                 [(v,[v']) | (v,v')<-arcs] ++ [(v',[v]) | (v,v')<-arcs]

-- Collect all nodes.
nodes :: Graph a -> [a]
nodes = Map.keys 

-- Collect all edges, avoiding duplicates due to simetry.
edges :: Ord a => Graph a -> [Edge a]
edges g = [(v,v') | (v,vs)<-Map.assocs g, v'<-vs, v<v']

-- Map a function over nodes of a graph.
mapNodes :: Ord b => (a -> b) -> Graph a -> Graph b
mapNodes f g = Map.mapKeys f $ Map.map (map f) g

-- Renumber graph nodes to 1, 2, ... etc.
renumber :: Ord a => Graph a -> Graph Int
renumber g = mapNodes (num!) g
  where num = Map.fromList $ zip (nodes g) [1..]

-- Delete edges.
delEdge :: Ord a => Edge a -> Graph a -> Graph a
delEdge (v,v') = Map.update (\ns -> Just (delete v' ns)) v .
                 Map.update (\ns -> Just (delete v ns)) v'

      
-- Regular triangular mesh.
-- Each node connects to 6 neighbours.
mesh :: Int -> Int -> Graph (Int,Int)
mesh w h = makeGraph [(p, p') | x<-[0..w-1], y<-[0..h-1], 
                      let p = (x,y), p'<-neighbs p]
    where neighbs (x,y) = filter inside [(x+1,y), (x,y+1), (x+1,y+1)]
          inside (x,y) = x>=0 && x<w && y>=0 && y<h
        


-- Generate a random mesh.
-- Start with a triangular mesh and remove some edges;
-- 1st arg is the fraction of edges to delete;
-- 2nd arg is the sqrt of number of nodes.
genMesh :: Float -> Int -> Gen (Graph Int)
genMesh r size = cut n g
    where g = renumber (mesh size size)
          n = floor $ r * fromIntegral (length (edges g))
          cut 0 g = return g
          cut n g | n>0 = do v <- elements (Map.keys g)
                             let vs = g!v
                             v' <- elements vs
                             let vs' = g!v'
                             let g' = delEdge (v,v') g
                             -- ensure nodes have degree >= 2 and
                             -- graph remains connected
                             if length vs>2 && length vs'>2 && 
                                connected g' v v'
                               then cut (n-1) g'
                               else cut (n-1) g


-- Check existence of a path 
-- to ensure that we generate connected graphs.
connected :: Ord a => Graph a -> a -> a -> Bool                  
connected g x y = Set.member y (reachable g x)

-- Breadth-first reachability search; 
-- not very efficient but will do for this example.
reachable :: Ord a => Graph a -> a -> Set a
reachable g v = bfs Set.empty (Set.singleton v)
  where bfs viz vs 
          | Set.null vs = viz
          | otherwise = bfs viz' vs'
            where viz' = Set.union viz vs
                  vs' = Set.fromList [n | v<-Set.elems vs, n<-g!v, 
                                         Set.notMember n viz'] 
        
-- Generate a graphviz input file for the graph.
toGraphviz :: (Ord a, Show a) => Graph a -> String
toGraphviz g = unlines $ 
               "graph {" : 
               "node [shape=circle,width=0.5,height=0.5,fixedsize=true];" : 
               arcs ++ ["}"]
  where arcs = [ ss x ++ " -- " ++ ss y ++ ";" | (x,y)<-edges g]
        ss = show . show   -- add quotes 


-- Make some graphs.
makeTests :: Int -> IO [Graph Int]
makeTests size = sequence [liftM head (sample' $ genMesh r size) 
                          | r<-[0, 0.1, 0.2, 0.5]]

-- Run graphviz to produce SVG files
main :: IO ()
main = do
     gs <- makeTests 5      -- make 5x5 random meshes
     sequence_ [ writeFile (dotfile i) (toGraphviz g) >> system (mksvg i)
                    | (i,g)<-zip [1..] gs ]
  where dotfile i = "input"++show i ++ ".dot"
        svgfile i = "output"++show i++ ".svg"
        mksvg i = "neato " ++ dotfile i ++ " -Tsvg -o " ++ svgfile i

       