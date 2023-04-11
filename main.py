import subprocess
import shutil
import os


def compile_project(project_directory, compile_task):
    """Compile a project using Gradle"""
    process = subprocess.Popen(args=['./gradlew', '--rerun-tasks', compile_task],
                               stdout=subprocess.PIPE,
                               stderr=subprocess.PIPE,
                               cwd=project_directory)
    process.communicate()
    if process.returncode != 0:
        raise ChildProcessError


def copy_files(source_directory, target_directory):
    """Copy files from one directory to another, preserving metadata"""
    if os.path.exists(target_directory):
        shutil.rmtree(target_directory)
    shutil.copytree(source_directory, target_directory, copy_function=shutil.copy2)


def generate_html(width, height, target_directory, html_file_name):
    """Generate HTML code"""
    return "<iframe " \
           "width={width} " \
           "height={height} " \
           "frameBorder=0 " \
           "src=\"{target_directory}/{html_file_name}\">" \
           "</iframe>".format(
                width=width,
                height=height,
                target_directory=target_directory,
                html_file_name=html_file_name,
           )


def define_env(env):
    """Compile, copy and generate HTML tag needed for embedding Compose Multiplatform content"""

    @env.macro
    def compose_mpp_sample(
            project_directory,
            project_output_directory,
            compile_task,
            width,
            height,
            target_directory,
            html_file_name,
    ):
        compile_project(project_directory, compile_task)
        site_target_directory = os.path.join(env.variables.config.site_dir, target_directory)
        copy_files(project_output_directory, site_target_directory)
        html_target_directory = os.path.relpath(site_target_directory, env.variables.config.site_dir)
        return generate_html(width, height, html_target_directory, html_file_name)
