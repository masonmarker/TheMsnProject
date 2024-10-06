"""Command line interface entry point for MSN2."""

# CLI
import click

# msn2 execution
from msn2python import run
from msnint2 import Interpreter


@click.command(help=".msn2 file runner and interpreter", 
               epilog="Find more documentation at https://www.masonmarker.com/projects/msn2")
@click.argument("action", type=str, default="run")
# click argument for the file, can be --file or -f
@click.option("--file", "-f", type=str, multiple=True,
              help="The path to a .msn2 file to run (.msn2 is not required)")
# sys args to msn2
@click.option("--args", "-a", type=str, multiple=True,
              help="The arguments to pass to a respective .msn2 file")
# runs an msn2 snippet
@click.option("--snippet", "-s", type=str, multiple=True,
              help="An msn2 code snippet to run")
def main(action, file, args, snippet):
    """Main entry point for the CLI."""
    def _run(f, args, i):
        run(f, args[i] if args and i < len(args) else None)

    def _fix_file(f):
        return f if f.endswith(".msn2") else f + ".msn2"

    def _run_snippets(i: Interpreter):
        for s in snippet:
            i.interpret(s)

    if action == "run":
        # standard msn2 program execution procedure,
        for i, f in enumerate(file):
            _run(_fix_file(f), args, i)
        # run snippets if they exist
        _run_snippets(Interpreter())
    # if the action is time, then we time all the scripts
    elif action == "time":
        import time
        # create an interpreter for printing
        i = Interpreter()

        def _timed_function(func, f):
            i.styled_print([{"text": "-" * 20, "fore": "black"}])
            i.styled_print(
                [{"text": f"Starting timed program: {f}", "fore": "blue"}])
            start = time.time()
            func()
            end = time.time()
            # msn2 color print
            i.styled_print(
                [{"text": f"Finished in: {end - start} seconds", "fore": "green"}])
            i.styled_print([{"text": "-" * 20, "fore": "black"}])

        # time each file
        for f in file:
            _timed_function(lambda: run(
                _fix_file(f), args[0] if args else None), f)
        i = Interpreter()
        # do the same for the snippets
        _timed_function(lambda: _run_snippets(i), "")
    # questions the integrity of the msn2 interpreter
    elif action == "verify":
        run('tests/validator.msn2')
    # gets a mini, portable msn2 package
    elif action == "portable":
        run('portable/port.msn2')
    # runs a quick msn2 test
    elif action == "test":
        run('tests/misc.msn2')
    # runs the msn2 interpreter
    elif action == "int":
        run('system/int.msn2')
    # runs the msn2 file browser
    elif action == "file":
        run('system/file.msn2')
    # runs the msn2 console tutorial
    elif action == "help":
        run('TUTORIAL/driver.msn2')
    # runs the msn2 gpt interaction
    elif action == "gpt":
        run('system/gpt.msn2')
    else:
        raise ValueError("Invalid action.")

if __name__ == "__main__":
    main()
